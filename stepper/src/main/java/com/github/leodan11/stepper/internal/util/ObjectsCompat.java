package com.github.leodan11.stepper.internal.util;

import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * This class consists of {@code static} utility methods for operating
 * on objects.
 * <p>
 * This backports {@link java.util.Objects} which is available since API 19.
 *</p>
 *
 */
public final class ObjectsCompat {

    private ObjectsCompat() {
    }

    /**
     * Returns {@code true} if the arguments are equal to each other
     * and {@code false} otherwise.
     * Consequently, if both arguments are {@code null}, {@code true}
     * is returned and if exactly one argument is {@code null}, {@code
     * false} is returned.  Otherwise, equality is determined by using
     * the {@link Object#equals equals} method of the first
     * argument.
     *
     * @param a an object
     * @param b an object to be compared with {@code a} for equality
     * @return {@code true} if the arguments are equal to each other
     * and {@code false} otherwise
     * @see Object#equals(Object)
     */
    @SuppressWarnings("PMD.SuspiciousEqualsMethodName")
    public static boolean equals(@Nullable Object a, @Nullable Object b) {
        return Objects.equals(a, b);
    }
}
