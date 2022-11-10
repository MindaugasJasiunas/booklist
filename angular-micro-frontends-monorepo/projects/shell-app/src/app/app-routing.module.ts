import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainPageComponent } from './main-page/main-page.component';
import { loadRemoteModule } from '@angular-architects/module-federation';
import { NotFoundComponent } from './not-found/not-found.component';

const routes: Routes = [
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
  {
    path: `auth`,
    loadChildren: () =>
      loadRemoteModule({
        type: 'module',
        remoteEntry: 'http://localhost:4203/remoteEntry.js',
        exposedModule: './auth.module',
      })
        .then((m) => m.AuthModule)
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
