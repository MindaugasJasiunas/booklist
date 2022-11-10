import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { MainPageComponent } from './main-page/main-page.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NotFoundComponent } from './not-found/not-found.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoggedInGuard } from './auth/guard/logged-in.guard';
import { AuthInterceptor } from './auth/interceptor/auth.interceptor';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { IsBooklisterGuard } from './auth/guard/is-booklister.guard';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    MainPageComponent,
    NotFoundComponent,
    LoginComponent,
    RegisterComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
  ],
  providers: [
    LoggedInGuard,
    IsBooklisterGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
