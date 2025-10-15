import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { AccountTS } from "../../types/tstypes/Accountts";
import { CommonModule } from "@angular/common";

@Component({
    selector: 'app-account',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './account.component.html',
    styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {
    accountForm!: FormGroup;
    account: AccountTS | undefined;

    constructor(private fb: FormBuilder) {}

    ngOnInit(): void {
        this.accountForm = this.fb.group({
            account_id: ['', [Validators.required]],
            customer_id: ['', [Validators.required]],
            balance: [0, [Validators.required, Validators.min(0)]]
        });
        this.account = new AccountTS("1", 1000.00, "1");
    }

    onSubmit(): void{
        if (this.accountForm.valid) {
            this.account = new AccountTS(this.accountForm.get('customer_id')?.value, this.accountForm.get('balance')?.value, this.accountForm.get('account_id')?.value);
        }
    }
}
