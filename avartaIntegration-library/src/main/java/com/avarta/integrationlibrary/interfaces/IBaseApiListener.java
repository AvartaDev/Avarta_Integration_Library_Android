package com.avarta.integrationlibrary.interfaces;

/**
 * Used for the api requests to handle the server response
 */
public interface IBaseApiListener<T> {
    void onSuccess(T data);

    void onError(Exception exception);
}
