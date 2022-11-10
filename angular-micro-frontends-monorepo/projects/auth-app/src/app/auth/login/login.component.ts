import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  signInForm!: FormGroup<{ email: FormControl; password: FormControl }>;

  ngOnInit() {
    // initialize a form before rendering
    this.signInForm = new FormGroup({
      // controls (key-value pairs)
      email: new FormControl(null, [Validators.required, Validators.email]),
      password: new FormControl(null, Validators.required),
    });
  }

  onLogin() {
    console.log(this.signInForm.status);
    console.log(this.signInForm.value.email);
    console.log(this.signInForm.value.password);
  }
}
