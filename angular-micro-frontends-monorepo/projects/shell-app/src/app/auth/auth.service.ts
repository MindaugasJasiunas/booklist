import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'projects/shell-app/src/environments/environment';
import { map, Observable, of, switchMap, tap } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loginUrl = environment.loginUrl;
  private registerUrl = environment.registerUrl;
  private refreshAccessTokenUrl = environment.resetAccessTokenUrl;
  private jwtAccessToken: string | null = null;
  private jwtRefreshToken: string | null = null;
  private loggedInUserEmail: string | null = null;
  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) {}

  public login(loginRequest: {email: string; password: string}): Observable< HttpResponse<any>> {
    return this.http.post<any>(this.loginUrl, loginRequest, {observe: 'response'});
  }

  public register(registerRequest: {email: string; password: string, firstName: string, lastName: string}): Observable< HttpResponse<any>> {
    return this.http.post<any>(this.registerUrl, registerRequest, {observe: 'response'});
  }

  public logout(): void {
    this.jwtRefreshToken = null;
    this.jwtAccessToken = null;
    this.loggedInUserEmail = null;

    // remove items from localStorage - token, user information
    localStorage.removeItem('user');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('accessToken');
  }

  public getAccessToken(refreshToken: string): Observable< HttpResponse< any>> {
    this.isLoggedIn();
    return this.http.post< any>(this.refreshAccessTokenUrl, null, {
      observe: 'response',
      headers: new HttpHeaders({ Authorization: refreshToken }),
    });
  }

  public saveRefreshToken(refreshToken: string): void {
    this.jwtRefreshToken = refreshToken;
    localStorage.setItem('refreshToken', refreshToken);
  }

  public saveAccessToken(accessToken: string): void {
    this.jwtAccessToken = accessToken;
    localStorage.setItem('accessToken', accessToken);
  }

  public addEmailToLocalCache(email: string): void {
    this.loggedInUserEmail = email;
    localStorage.setItem('user', JSON.stringify(email));
  }

  public getEmailFromLocalCache(): string {
    return JSON.parse(localStorage.getItem('user')!);
  }

  public loadAccessTokenFromLocalCache(): string {
    this.jwtAccessToken = localStorage.getItem('accessToken')!;
    return this.jwtAccessToken;
  }

  public loadRefreshTokenFromLocalCache(): string {
    this.jwtRefreshToken = localStorage.getItem('refreshToken')!;
    return this.jwtRefreshToken;
  }

  public isLoggedIn(): boolean {
    // if refresh token is valid - user is logged in
    this.loadRefreshTokenFromLocalCache();
    if (this.jwtRefreshToken != null && this.jwtRefreshToken !== '') {
      if (this.jwtHelper.decodeToken(this.jwtRefreshToken).sub != null || '') {
        if (!this.jwtHelper.isTokenExpired(this.jwtRefreshToken)) {
          this.addEmailToLocalCache(this.jwtHelper.decodeToken(this.jwtRefreshToken).sub);
          return true;
        }
      }
    }
    // if there is no token - logout
    this.logout();
    return false;
  }

  public isTokenExpired(token: string | undefined) {
    return this.jwtHelper.isTokenExpired(token);
  }

  public isBooklister(): Observable<boolean>{
    const token = this.loadAccessTokenFromLocalCache();
    if(token == null || this.isTokenExpired(token)){
      const refreshToken = this.loadRefreshTokenFromLocalCache();
      if(refreshToken == null || this.isTokenExpired(refreshToken)) return of(false);
      return this.getAccessToken(refreshToken).pipe(
        tap(accessResponse => this.saveAccessToken(accessResponse.headers.get('authorization')!)),
        map(accessResponse => {
          const accessToken = accessResponse.headers.get('authorization')!;
          const authorities = (this.jwtHelper.decodeToken(accessToken).authorities as [prop: string]);
          const found = authorities.find(value => value === "book:read");
          if(found !== undefined && found.length > 0){
            return true;
          }
          return false;
        })
      );
    }
    return of(true);
  }

}

export type responseTypes = {
  notEnoughOfPrivileges: boolean,
  notLoggedIn: boolean,
  successRegister: boolean
}
