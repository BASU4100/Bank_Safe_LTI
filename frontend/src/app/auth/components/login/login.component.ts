import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Store } from "@ngrx/store";
import { Observable, catchError, of, switchMap, tap, throwError } from "rxjs";
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth.service";

@Component({
    selector: "app-login",
    templateUrl: "./login.component.html",
    styleUrls: ["./login.component.scss"],
})
export class LoginComponent implements OnInit {
    loginForm: FormGroup;
    loginError$: Observable<{ [key: string]: string }>;

    constructor(
        private formBuilder: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loginForm = this.formBuilder.group({
            username: ["", [Validators.required]],
            password: ["", Validators.required],
        });
    }

    onSubmit(): void {
        if (this.loginForm.invalid) {
            this.loginError$ = of({
                message: "Please make sure you have filled all the required fields",
            });
            return;
        } else {
            const { username, password } = this.loginForm.value;
            this.loginError$ = this.authService
                .login({ username, password })
                .pipe(
                    tap((response) => {
                        console.log(response);
                        localStorage.setItem("token", response.token);
                        localStorage.setItem("role", response.roles);
                        localStorage.setItem("user_id", response.userId);
                        console.log(localStorage.getItem("role"));
                        this.router.navigate(["/bank"]);
                    }),
                    catchError((error) => {
                        console.error("Login error:", error);
                        return of({ message: "Please check the username and password" });
                    })
                );
        }
    }
}