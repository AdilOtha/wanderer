package ca.dal.cs.wanderer.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessMessages {
    // Success messages
    PIN_SAVE_SUCCESS("Pin saved successfully"),
    PINS_RETRIEVE_SUCCESS("Pins retrieved successfully"),
    SINGLE_PIN_RETRIEVE_SUCCESS("Pin retrieved successfully"),
    PIN_DELETE_SUCCESS("Pin deleted successfully"),
    PIN_UPDATE_SUCCESS("Pin updated successfully"),
    PIN_RATING_SUCCESS("Pin rating updated successfully");
    private final String successMessage;
}
