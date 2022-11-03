import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

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
    console.log(this.signUpForm.status);
    console.log(this.signUpForm.value.firstName);
    console.log(this.signUpForm.value.lastName);
    console.log(this.signUpForm.value.email);
    console.log(this.signUpForm.value.password);
  }
}
