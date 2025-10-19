import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AuthService } from "../../services/auth.service";
import { ReactiveFormsModule, FormsModule } from "@angular/forms";
import { Router } from "@angular/router";


@Component({
    selector: "app-user",
    templateUrl: "./user.component.html",
    styleUrls: ["./user.component.scss"],
})
export class UserComponent implements OnInit {
    userForm: FormGroup;
    successMessage: string | null = null;
    errorMessage: string | null = null;

    constructor(
        private formBuilder: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.userForm = this.formBuilder.group({
            username: ["", [Validators.required, this.noSpecialCharacters]],
            password: ["", [Validators.required, Validators.minLength(8)]],
            name: ["", [Validators.required]],
            email: ["", [Validators.required, Validators.email]],
            role: ["", [Validators.required]],
        });
    }

    private noSpecialCharacters(control: any): {[key: string]: boolean} | null {
        if (control.value && /[\W_]/.test(control.value)) {
            return { specialCharacters: true };
          }
          return null;
    }

    onSubmit(): void {
        if (this.userForm.valid) {
            this.authService.createUser(this.userForm.value).subscribe({
                next: (response) => {
                    this.successMessage = "User created successfully";
                    this.userForm.reset();
                    this.errorMessage = "";
                    this.router.navigate(['/auth/login']);
                },
                error: (error) => {
                    console.log(error);
                    this.errorMessage = error.error ?? "Please fill the form correctly";
                }
            });
        }
        else {
            this.errorMessage = "Please fill the form correctly";
        }
    }
}