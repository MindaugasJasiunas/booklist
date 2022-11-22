import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainPageComponent } from './main-page/main-page.component';
import { loadRemoteModule } from '@angular-architects/module-federation';
import { NotFoundComponent } from './not-found/not-found.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { environment } from '../environments/environment';

const routes: Routes = [
  { component: LoginComponent, path: 'login' },
  { component: RegisterComponent, path: 'register' },
  {
    path: `books`,
    loadChildren: () =>
      loadRemoteModule({
        type: 'module',
        remoteEntry: environment.booksRemoteEntry,
        exposedModule: './books.module',
      })
        .then((m) => m.BooksModule)
        .catch((err) => console.log('ERROR:', err)),
  },
  { path: ``, component: MainPageComponent, pathMatch: 'full' },
  {
    path: '**',
    component: NotFoundComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
