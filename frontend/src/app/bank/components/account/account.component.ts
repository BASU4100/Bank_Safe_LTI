import { Component, OnInit } from '@angular/core';
import { Account } from '../../types/Account';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BankService } from '../../services/bank.service';
import { Customer } from '../../types/Customer';

@Component({
  selector: 'app-accounts',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {
  accountForm!: FormGroup;
  account: Account | undefined;
  customers: Customer[] = [];
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private banksService: BankService
  ) { }

  ngOnInit(): void {
    this.loadCustomers();
    this.accountForm = this.formBuilder.group({
      customer: [null, [Validators.required]],
      balance: ["", [Validators.required, Validators.min(0)]],
    });
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
      this.banksService.addAccount(this.accountForm.value).subscribe({
        next: (response) => {
          this.successMessage = 'Account created successfully';
          this.errorMessage = '';
          this.accountForm.reset();
        },
        error: (error) => this.errorMessage = error.error
      });
    } else {
      this.errorMessage = 'Please fill out all required fields correctly.';
      this.successMessage = '';
    }
  }
}