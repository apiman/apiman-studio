/**
 * Bootstraps the application and makes the ROUTER_PROVIDERS and the APP_BASE_HREF available to it.
 * @see https://angular.io/docs/ts/latest/api/platform-browser-dynamic/index/bootstrap-function.html
 */
import { enableProdMode } from '@angular/core';
// The browser platform with a compiler
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

// The app module
import { StudioModule } from './studio.module';

if (String('<%= BUILD_TYPE %>') === 'prod') {
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(StudioModule /*, options*/);

// In order to start the Service Worker located at "/ngsw-worker.js"
// uncomment this line. More about Service Workers here
// https://developer.mozilla.org/en-US/docs/Web/API/Service_Worker_API/Using_Service_Workers
//
// if ('serviceWorker' in navigator) {
//   const workerScript = '/ngsw-worker.js';
//   (<any>navigator).serviceWorker
//     .register(workerScript)
//     .then((registration: any) => console.log('ServiceWorker registration successful with scope: ', registration.scope))
//     .catch((err: any) => console.log('ServiceWorker registration failed: ', err));
// }