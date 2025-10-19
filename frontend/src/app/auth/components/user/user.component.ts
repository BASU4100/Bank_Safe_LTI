import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AuthService } from "../../services/auth.service";

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
        private authService: AuthService
    ) { }

    ngOnInit(): void {
        this.userForm = this.formBuilder.group({
            username: ["", [Validators.required, this.noSpecialCharacters]],
            password: ["", [Validators.required, Validators.minLength(8)]],
            role: ["", [Validators.required]],
            name: ["", [Validators.required]],
            email: ["", [Validators.required, Validators.email]],
        });
    }

    private noSpecialCharacters(control: any): { [key: string]: boolean } | null {
        const SPECIAL_CHARACTERS_REGEX = /[!@#$%^&*()_+{}\[\]:;<>,.?~\\/-]/;
        if (SPECIAL_CHARACTERS_REGEX.test(control.value)) {
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