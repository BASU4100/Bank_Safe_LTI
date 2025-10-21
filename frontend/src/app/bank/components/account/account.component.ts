import { Component, OnInit } from '@angular/core';
import { Account } from '../../types/Account';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BankService } from '../../services/bank.service';
import { Customer } from '../../types/Customer';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

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
  isFormSubmitted: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private banksService: BankService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCustomers();
    this.accountForm = this.formBuilder.group({
      customer: ['', [Validators.required]],
      balance: [0, [Validators.required, Validators.min(0)]],
    });
  }

  loadCustomers(): void {
    this.banksService.getAllCustomers().subscribe({
      next: (response) => {
        this.customers = response;
        console.log(this.customers);
      },
      error: () => console.log('Error in loading customers')
    })
  }

  onSubmit(): void {
    this.isFormSubmitted = true;
    if (this.accountForm.valid) {
      this.account = new Account(this.accountForm.value);
      console.log(this.account);
      this.banksService.addAccount(this.account).subscribe({
        next: () => {
          this.successMessage = 'Account created successfully';
          this.errorMessage = '';
          this.accountForm.reset();
          setTimeout(() => {
            this.successMessage = '';
            this.router.navigate(['/bank']);
          }, 1500);
        },
        error: (error) => this.errorMessage = error.errorMessage
      });
    } else {
      this.errorMessage = 'Please fill out all required fields correctly.';
      this.successMessage = '';
    }
  }
}