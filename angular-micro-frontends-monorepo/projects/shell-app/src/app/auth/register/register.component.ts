import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, map, tap } from 'rxjs';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  signUpForm!: FormGroup<{
    email: FormControl;
    password: FormControl;
    firstName: FormControl;
    lastName: FormControl;
  }>;
  errorMsg: string | null = null;

  constructor(private service: AuthService, private router: Router) {
    if(service.isLoggedIn()) router.navigate(['/']);
  }

  ngOnInit() {
    // initialize a form before rendering
    this.signUpForm = new FormGroup({
      // controls (key-value pairs)
      email: new FormControl(null, [Validators.required, Validators.email]),
      password: new FormControl(null, Validators.required),
      firstName: new FormControl(null, Validators.required),
      lastName: new FormControl(null, Validators.required),
    });
  }

  onRegister() {
    if(!this.signUpForm.valid) return;
    const email: string = this.signUpForm.value.email;
    const password: string = this.signUpForm.value.password;
    const firstName: string = this.signUpForm.value.firstName;
    const lastName: string = this.signUpForm.value.lastName;
    this.service.register({
      email: email,
      firstName: firstName,
      lastName: lastName,
      password: password,
    })
    .pipe(
      map(response => {
        if(response.ok) this.router.navigate(['/login'],{ queryParams: { successRegister: 'true' } });
      }),
      catchError((err: HttpErrorResponse) => this.errorMsg = err.message)
    ).subscribe();
  }

  showError(): string | null {
    return this.errorMsg;
  }

}

