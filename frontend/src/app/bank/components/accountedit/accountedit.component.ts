import { Component, OnInit } from '@angular/core';
import { Account } from '../../types/Account';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BankService } from '../../services/bank.service';
import { Customer } from '../../types/Customer';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'app-accountedit',
    templateUrl: './accountedit.component.html',
    styleUrls: ['./accountedit.component.scss']
})
export class EditAccountComponent implements OnInit {
    accountForm!: FormGroup;
    account: Account | undefined;
    customers: Customer[] = [];
    accountId: number;
    errorMessage: string = '';
    successMessage: string = '';

    constructor(
        private formBuilder: FormBuilder,
        private banksService: BankService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loadCustomers();
        this.accountForm = this.formBuilder.group({
            customer: [null, [Validators.required]],
            balance: ["", [Validators.required, Validators.min(0)]],
        });
        this.route.params.subscribe(params => {
            this.accountId = params.accountId;
            this.loadAccountDetails();
        });
    }

    loadAccountDetails(): void {
        this.banksService.getAccountById(this.accountId).subscribe({
            next: (account) => {
                this.account = account;
                this.accountForm.patchValue({
                    customer: account.customer,
                    balance: account.balance
                });
            },
            error: (error) => {
                this.errorMessage = 'Error loading account details.';
                console.log(this.errorMessage);
            }
        })
    }

    loadCustomers(): void {
        this.banksService.getAllCustomers().subscribe({
            next: (response) => {
                this.customers = response;
            },
            error: (error) => console.log('Error in loading customers')
        })
    }

    onSubmit(): void {
        if (this.accountForm.valid) {
            const updatedAccount: Account = {
                accountId: this.account?.accountId,
                customer: this.accountForm.value.customer,
                balance: this.accountForm.value.balance
            }
            this.banksService.editAccount(updatedAccount).subscribe({
                next: (response) => {
                    this.account = response;
                    this.successMessage = 'Account updated successfully';
                    this.errorMessage = '';
                    this.accountForm.reset();
                    this.router.navigate(['/bank']);
                },
                // error: (error) => {
                //     if (error.status === 400) {
                //         this.errorMessage = error.error;
                //     }
                //     else {
                //         this.errorMessage = 'Please check the entered data.';
                //     }
                // }
            });
        } else {
            this.errorMessage = 'Please fill out all required fields correctly.';
            this.successMessage = '';
        }
    }
}