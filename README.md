# react-native-share-android
An image sharing module for React Native on Android.

A react native sharing package for android that supports image sharing. Meant to bridge the gap between the IOS and Android functionality of the built in Share api

## Contraints

The goal of this module is to polyfill the React Native Share api to support image sharing on Android, but this module, as of version 0.1.0 has several constraints.
1. The module currently only allows the sharing of jpeg images.  In order to share other types of files you need to change the mime type to match the type of the file being shared.
2. The module currently only gives permission to share images stored locally in the applications cache.  In order to share files from a different location you need to update the FileProvider accordingly.  

## Usage

1. Ensure you've made the appropriate native code changes to utilize this package.  The `NodeJS-react-native-share-android-0.1.0_android` branch contains the native code and Config that needs be built into the app.

2. In your Javascript bundle ensure the following:

    In your Config file ensure you have defined a dependency on

    ```javascript
    NodeJS-react-native-share-android = 0.1.0_js;
    ```

    To use the react-native-share-android module create a file called `Share.js` containing:

    ```javascript
    import {
        Platform,
        Share as ShareIOS,
    } from 'react-native';

    import ShareAndroid from 'react-native-share-android';

    const Share = (Platform.OS === 'ios') ? ShareIOS : ShareAndroid;

    export default Share;
    ```

    import and use this Share module instead of the react-native share module.
