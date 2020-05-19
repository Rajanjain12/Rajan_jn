import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { LoaderService } from 'src/app/core/services/loader.service';

@Injectable()
export class LoaderInterceptor implements HttpInterceptor {
  constructor(public loaderService: LoaderService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.loaderService.disable === false) {
      this.loaderService.show();
    }
    return next.handle(req).pipe(
      finalize(() => {
        this.loaderService.hide();
        this.loaderService.disable = false;
      })
    );
  }
}
