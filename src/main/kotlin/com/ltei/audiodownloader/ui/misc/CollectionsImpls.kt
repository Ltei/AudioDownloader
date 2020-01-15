package com.ltei.audiodownloader.ui.misc

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.collections.ObservableSet

/**
 * Returns a new [ObservableList] that is backed by the original list.
 *
 * **Note:** If the original list is read-only, attempting to modify the returned list will result in an [UnsupportedOperationException]
 */
fun <T> List<T>.asObservable(): ObservableList<T> = FXCollections.observableList(this)

/**
 * Returns a new [ObservableSet] that is backed by the original set.
 *
 * **Note:** If the original set is read-only, attempting to modify the returned set will result in an [UnsupportedOperationException]
 */
fun <T> Set<T>.asObservable(): ObservableSet<T> = FXCollections.observableSet(this)

/**
 * Returns a new [ObservableMap] that is backed by the original map.
 *
 * **Note:** If the original map is read-only, attempting to modify the returned map will result in an [UnsupportedOperationException]
 */
fun <K, V> Map<K, V>.asObservable(): ObservableMap<K, V> = FXCollections.observableMap(this)
