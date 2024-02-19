/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androhub.networkmodule.base;


public interface BaseView {
    /**
     * show error message
     * @param msg  msg to display
     */
    void showErrorMsg(String msg);

    /**
     * show error message
     * @param msg  msg string resource id
     */
    void showErrorMsg(int msg);

    /**
     * show success message
     * @param msg  msg to display
     */
    void showSuccessMsg(String msg);

    /**
     * show error message
     * @param msg  msg string resource id
     */
    void showSuccessMsg(int msg);

    /**
     * show loading indicator
     */
    void showLoading();

    /**
     * hide loading indicator
     */
    void hideLoading();


}
