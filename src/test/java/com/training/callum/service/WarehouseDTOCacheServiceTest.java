package com.training.callum.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.callum.whoms.config.InputProperties;
import com.training.callum.whoms.service.ExtDataValidationService;
import com.training.callum.whoms.service.WarehouseDTOCacheService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for WarehouseDTOCacheService:
 * - verifies validation is invoked when a valid resource is present
 * - proves bad filename and missing file paths are logged and do not throw
 *
 * These tests assume:
 * - InputProperties is a record with a nested File record and a file() accessor.
 * - File record exposes warehouses() accessor for the warehouses path.
 * - WarehouseDTOCacheService has a loadWarehouseDtos() method (annotated @PostConstruct in production).
 * - WarehouseDTOCacheService stores cached list in a List field (detected via reflection).
 */
@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class WarehouseDTOCacheServiceTest {

    @Mock
    private InputProperties inputProperties;

    @Mock
    private InputProperties.File inputFile;

    @Mock
    private ExtDataValidationService extValidationService;

    private ObjectMapper objectMapper;
    private ResourceLoader resourceLoader;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        resourceLoader = new DefaultResourceLoader();
    }

    @Test
    void shouldLoadAndValidate_whenTestResourceExists() throws Exception {
        // given: test file placed at src/test/resources/input/test-warehouses.json (step 9)
        when(inputProperties.file()).thenReturn(inputFile);
        when(inputFile.warehouses()).thenReturn("classpath:input/test-warehouses.json");

        WarehouseDTOCacheService service = new WarehouseDTOCacheService(
                inputProperties, resourceLoader, objectMapper, extValidationService);

        // when: invoke the loader (method is @PostConstruct in prod; call directly)
        invokeLoadMethod(service);

        // then: validation service should be invoked for at least one item
        verify(extValidationService, atLeastOnce()).validate(any());

        // and: internal cache should contain at least one element
        List<?> cached = findCachedList(service);
        assertNotNull(cached, "expected a cached list inside service");
        assertFalse(cached.isEmpty(), "expected cached list to be populated from test resource");
    }

    @Test
    void shouldLogBadFileName_whenFilenameIsWrong(CapturedOutput output) throws Exception {
        // given a wrong filename extension
        when(inputProperties.file()).thenReturn(inputFile);
        when(inputFile.warehouses()).thenReturn("classpath:input/test-warehouses.json.invalid");

        WarehouseDTOCacheService service = new WarehouseDTOCacheService(
                inputProperties, resourceLoader, objectMapper, extValidationService);

        // when
        invokeLoadMethod(service);

        // then: the configured path should be present in logs
        String logs = output.getOut() + output.getErr();
        assertTrue(logs.contains("test-warehouses.json.invalid") || logs.contains("test-warehouses.json.invalid"),
                () -> "Expected logs to mention the bad filename; logs:\n" + logs);

        // and nothing was cached
        List<?> cached = findCachedList(service);
        assertTrue(cached == null || cached.isEmpty(), "expected empty cache for bad filename");
    }

    @Test
    void shouldLogMissingFile_whenPathDoesNotExist(CapturedOutput output) throws Exception {
        // given a non-existent absolute path (won't exist on CI)
        when(inputProperties.file()).thenReturn(inputFile);
        when(inputFile.warehouses()).thenReturn("file:/nonexistent/path/does-not-exist.json");

        WarehouseDTOCacheService service = new WarehouseDTOCacheService(
                inputProperties, resourceLoader, objectMapper, extValidationService);

        // when
        invokeLoadMethod(service);

        // then: logs should mention the configured path
        String logs = output.getOut() + output.getErr();
        assertTrue(logs.contains("does-not-exist.json") || logs.contains("nonexistent"),
                () -> "Expected logs to mention the missing path; logs:\n" + logs);

        // and nothing was cached
        List<?> cached = findCachedList(service);
        assertTrue(cached == null || cached.isEmpty(), "expected empty cache for missing file");
    }

    // helper to invoke the loadWarehouseDtos method (PostConstruct in production)
    private static void invokeLoadMethod(Object service) throws Exception {
        Method m = null;
        for (Method method : service.getClass().getDeclaredMethods()) {
            if ("loadWarehouseDtos".equals(method.getName())) {
                m = method;
                break;
            }
        }
        if (m == null) {
            // try a no-arg public method fallback named "load" or "init"
            for (Method method : service.getClass().getDeclaredMethods()) {
                if ("load".equals(method.getName()) || "init".equals(method.getName())) {
                    m = method;
                    break;
                }
            }
        }
        if (m == null) {
            throw new IllegalStateException("Could not find load method (loadWarehouseDtos/load/init) on " + service.getClass());
        }
        m.setAccessible(true);
        m.invoke(service);
    }

    // helper to find first List field inside service instance
    @SuppressWarnings("unchecked")
    private static List<?> findCachedList(Object service) throws Exception {
        for (Field f : service.getClass().getDeclaredFields()) {
            if (List.class.isAssignableFrom(f.getType())) {
                f.setAccessible(true);
                return (List<?>) f.get(service);
            }
        }
        return null;
    }
}