package me.sheasmith.weatherstation.models;

import org.jetbrains.annotations.NotNull;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public interface ApiResponse<T> {
    void success(@NotNull T value);

    void error(Exception e);
}
