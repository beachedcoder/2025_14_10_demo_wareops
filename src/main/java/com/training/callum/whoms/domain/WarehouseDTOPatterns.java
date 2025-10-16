package com.training.callum.whoms.domain;

import java.util.regex.Pattern;

/**
 * Enum defining regex patterns and error messages for validating WarehouseDTO fields.
 */
public enum WarehouseDTOPatterns {
    NAME(Pattern.compile("^[a-zA-Z]+(?: [a-zA-Z]+)*$"), "Name must consist of alphabetic characters only, allowing multiple names separated by spaces."),
    ADDRESS(Pattern.compile("^[a-zA-Z0-9 ,.-]+$"), "Address must be a valid single-line US address format (alphanumeric, spaces, commas, periods, hyphens)."),
    CITY(Pattern.compile("^[a-zA-Z]+(?: [a-zA-Z.]+)*$"), "City must be a multi-part name with alphabetic characters, spaces, and optional abbreviation punctuation."),
    STATE(Pattern.compile("^[A-Z]{2}$"), "State must be a two-letter uppercase abbreviation."),
    POSTAL_CODE(Pattern.compile("^\\d{5}(-\\d{4})?$"), "Postal code must be a valid US format (5 digits, optional hyphen and 4 digits)."),
    WAREHOUSE_PHONE(Pattern.compile("^\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$"), "Warehouse phone must be a valid US phone number format (with or without parentheses and dashes)."),
    SQUARE_FOOTAGE(Pattern.compile("^\\d+$"), "Square footage must be a whole number (validation for range 1000-3000000 handled separately)."),
    LOADING_DOCKS(Pattern.compile("^\\d+$"), "Loading docks must be a whole number (validation for range 1-100 handled separately).");

    private final Pattern pattern;
    private final String errorMessage;

    WarehouseDTOPatterns(final Pattern pattern, final String errorMessage) {
        this.pattern = pattern;
        this.errorMessage = errorMessage;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}