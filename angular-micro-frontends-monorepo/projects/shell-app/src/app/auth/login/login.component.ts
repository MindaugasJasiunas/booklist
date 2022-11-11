import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, map, Observable, Observer, switchMap, tap } from 'rxjs';
import { AuthService, responseTypes } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  signInForm!: FormGroup<{ email: FormControl; password: FormControl }>;
  errorMsg: string | null = null;
  redirectErrorMsg$: Observable<string | null>;
  redirectSuccessMsg$: Observable<string | null>;

  constructor(private service: AuthService, private router: Router, private aRoute : ActivatedRoute){
    if(service.isLoggedIn()) router.navigate(['/']);

    this.redirectErrorMsg$ = this.aRoute.queryParams.pipe(
      map((response) => (response as responseTypes)),
      map(obj => {
        if(obj.notEnoughOfPrivileges){
          return "Not enough privileges";
        }else if(obj.notLoggedIn){
          return "Login required"
        }else{
          return null;
        }
      })
    );

    this.redirectSuccessMsg$ = this.aRoute.queryParams.pipe(
      map((response) => (response as responseTypes)),
      map(obj => {
        if(obj.successRegister){
          return "Successfully registered. Now you can login!"
        }else{
          return null;
        }
      })
    );

  }

  ngOnInit() {
    // initialize a form before rendering
    this.signInForm = new FormGroup({
      // controls (key-value pairs)
      email: new FormControl(null, [Validators.required, Validators.email]),
      password: new FormControl(null, Validators.required),
    });
  }

  onLogin() {
    if(!this.signInForm.valid) return;
    const email: string = this.signInForm.value.email;
    const password: string = this.signInForm.value.password;
    this.service.login({email: email, password: password}).pipe(
      tap(loginResponse => this.service.saveRefreshToken(loginResponse.headers.get('authorization')!)),
      switchMap(loginResponse => this.service.getAccessToken(loginResponse.headers.get('authorization')!)),
      tap(accessResponse => this.service.saveAccessToken(accessResponse.headers.get('authorization')!)),
      map(_ => this.router.navigate(['/'])),
      catchError((err: HttpErrorResponse) => this.errorMsg="Email or password is invalid")
    ).subscribe();


  }

  showError(): string | null {
    return this.errorMsg;
  }

}
