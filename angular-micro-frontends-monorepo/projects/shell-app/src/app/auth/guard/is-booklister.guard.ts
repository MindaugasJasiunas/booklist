import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { map, Observable } from "rxjs";
import { AuthService } from "../auth.service";

@Injectable({providedIn: 'root'})
export class IsBooklisterGuard implements CanActivate {

    constructor(private authService: AuthService, private router: Router){}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable< boolean | UrlTree> | Promise< boolean | UrlTree> {
      return this.authService.isBooklister().pipe(
        map(isBooklister => {
          if(isBooklister) return true;
          this.router.navigate(['/login'],{ queryParams: { notEnoughOfPrivileges: 'true' } });
          return false;
        })
      );
    }
}
