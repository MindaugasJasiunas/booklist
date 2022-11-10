import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  loginUrl = environment.loginUrl;
  registerUrl = environment.registerUrl;
  resetAccessTokenUrl = environment.resetAccessTokenUrl;

  constructor() { }

  login(){

  }

  register(){

  }

  resetAccessToken(){
    
  }
}
