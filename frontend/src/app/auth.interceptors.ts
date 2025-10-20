import { Injectable } from "@angular/core";
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
} from "@angular/common/http";
import { Observable } from "rxjs";
import { AuthService } from "./auth/services/auth.service";
 
@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {
 
  constructor(private authService: AuthService) {}
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();
    if (req.url.includes("login") || req.url.includes("register")) {
      return next.handle(req);
    }

    if (token) {
      req = req.clone({
        setHeaders: {
          "Content-Type": "application/json; charset=utf-8",
          Accept: "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
    }
 
    return next.handle(req);
  }
}