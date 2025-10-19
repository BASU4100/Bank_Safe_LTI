import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { BankService } from "../../services/bank.service";
import { Customer } from "../../types/Customer";
import { Account } from "../../types/Account";

@Component({
    selector: 'app-account',
    // standalone: true,
    // imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './account.component.html',
    styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {
    accountForm!: FormGroup;
    account: Account | undefined;
    customers: Customer[];
    errorMessage: string;
    successMessage: string;
    constructor(private formBuilder: FormBuilder, private banksService: BankService){}

    ngOnInit(): void {
        this.accountForm = this.formBuilder.group({
            // account_id: ['', [Validators.required]],
            customer: ['', [Validators.required]],
            balance: ['', [Validators.required, Validators.min(0)]]
        });
        // this.account = new AccountTS("1", 1000.00, "1");
        this.loadCustomers();
    }

    onSubmit(): void {
        if (this.accountForm.valid) {
        //   this.account = new Account(this.accountForm.value);
            this.banksService.addAccount(this.accountForm.value).subscribe(data =>
            this.account=data
            )
            this.successMessage = 'Account created successfully';
            this.errorMessage = '';
        } else {
            this.successMessage = '';
            this.errorMessage = 'Please fill out all required fields correctly.';
        }
    }

    loadCustomers(): void {
        // this.banksService.getAllCustomers().subscribe(data =>
        //     this.customers=data
        // )
    }
}
