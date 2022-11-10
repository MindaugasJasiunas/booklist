import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'projects/shell-app/src/environments/environment';
import { Observable, take, tap, switchMap, throwError, map, catchError, of } from 'rxjs';
import { AuthService } from '../auth.service';


@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}

  // runs before request leaves
  intercept(request: HttpRequest< any>, next: HttpHandler): Observable< HttpEvent< any>> {
    console.log('intercept filtering: '+request.url);
    if (
      request.url.includes(environment.loginUrl) ||
      request.url.includes(environment.registerUrl) ||
      request.url.includes(environment.resetAccessTokenUrl) ||
      //dont intercept GET requests for book or books
      request.url.includes(environment.getBookUrl) ||
      request.url.includes(environment.getBooksUrl)
    ) {
      // pass request through
      return next.handle(request);
    }

    let jwtToken = this.authService.loadAccessTokenFromLocalCache();
    let modifiedRequest = request.clone({
      setHeaders: { Authorization: `${jwtToken}` },
    });

    if (this.authService.isTokenExpired(jwtToken?.substring(7))) {
      // handle expired JWT before sending request
      this.authService.loadRefreshTokenFromLocalCache();
      const refreshToken = this.authService.loadRefreshTokenFromLocalCache();
      if (!refreshToken) {
        // no refresh token - logout & pass through
        this.authService.logout();
        this.router.navigate(['/login']);
        // return next.handle(request);
        throw throwError(() => new Error('no refresh token')); // dont send request
      }

      return this.authService.getAccessToken(refreshToken).pipe(
        take(1),
        // if authentication service is down - can't refresh token - automatically logout
        catchError(
          (error: HttpErrorResponse): Observable< any> => {
              this.authService.logout();
              return throwError(() => error);
          },
        ),
        tap((response: HttpResponse< any>) => {
          this.authService.saveAccessToken(
            response.headers.get('authorization')!
          )
        }),
        switchMap((response: HttpResponse<any>) => {
          // return request with updated access token
          const newRequest = request.clone({
            setHeaders: {
              Authorization: response.headers.get('authorization')!,
            },
          });
          return next.handle(newRequest);
        })
      );
    }

    // pass request through
    return next.handle(modifiedRequest);
  }
}
