import { ApplicationConfig, provideZoneChangeDetection, importProvidersFrom } from '@angular/core';
import { RouteReuseStrategy, provideRouter, withPreloading, PreloadAllModules } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { IonicRouteStrategy, provideIonicAngular } from '@ionic/angular/standalone';
import { IonicStorageModule } from '@ionic/storage-angular';
import { routes } from './app.routes';
import { authInterceptor } from './interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
    providers: [
        { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideRouter(routes, withPreloading(PreloadAllModules)),
        provideIonicAngular(),
        provideHttpClient(
            withInterceptors([authInterceptor])
        ),
        importProvidersFrom(IonicStorageModule.forRoot())
    ],
};
