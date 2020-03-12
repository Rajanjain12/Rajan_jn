// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  //serverUrl: 'http://44.232.86.255:8111/',
  serverUrl: 'http://localhost:8111/',
  pubnubSubscribeKey: 'sub-c-a408d714-2b8c-11ea-894a-b6462cb07a90',
  pubnubPublishKey: 'pub-c-cf81e7f1-47b0-4559-a127-720cd3085a92',
  pubnubIndividualChannel: 'RSNT_GUEST_DEV',
  pubnuGlobalChannel: 'RSNT_GLOBAL_DEV'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
