import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { MainPageComponent } from './main-page/main-page.component';
import { RegisterComponent } from './register/register.component';
import { loadRemoteModule } from '@angular-architects/module-federation';
import { NotFoundComponent } from './not-found/not-found.component';

const routes: Routes = [
  { path: `login`, component: LoginComponent },
  { path: `register`, component: RegisterComponent },
  {
    path: `books`,
    loadChildren: () =>
      loadRemoteModule({
        type: 'module',
        remoteEntry: 'http://localhost:4202/remoteEntry.js',
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
