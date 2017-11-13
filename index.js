import { NativeModules, findNodeHandle } from 'react-native';
const { ShareAndroidModule } = NativeModules;

export function share( content, options = { dialogTitle : '' }) {
    return ShareAndroidModule.shareAndroid(content, options.dialogTitle);
}

export const sharedAction = 'sharedAction';

export default { share, sharedAction };
