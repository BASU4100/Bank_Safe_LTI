import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BankService } from '../../services/bank.service';
import { Account } from '../../types/Account';
import { Customer } from '../../types/Customer';
import { Transaction } from '../../types/Transaction';
import { TransactionTS } from '../../types/tstypes/Transactionts';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss']
})
export class TransactionComponent implements OnInit {
  transactionForm: FormGroup;
  accounts: Account[];
  date:Date;
  role: string|null;
  userId: string|null;
  transactionError: string;
  transactionSuccess: string;
  users: Customer[];
  isFormSubmitted: boolean = false;

  constructor(
    private fb: FormBuilder,
    private bankService: BankService,
    private router: Router
    ) {}

  ngOnInit() {
    this.role = localStorage.getItem("role");
    this.userId = localStorage.getItem("user_id");
    if(this.role=='USER') {
      this.bankService.getAccountsByUser(this.userId).subscribe({
        next: (response) => {
          this.accounts = response;
        }
      })
    }
    this.transactionForm = this.fb.group({
      accounts: ['', Validators.required],
      amount: [null, [Validators.required, Validators.min(1)]],
      transactionType: [null, Validators.required],
    });
  }

  onSubmit() {
    this.isFormSubmitted = true;
    if (this.transactionForm.invalid) {
      return;
    } else {
      const data = this.transactionForm.value;
      console.log(data);
      data.transactionDate = new Date();
      const transaction: Transaction = new Transaction(data);
      console.log(transaction);
      this.bankService.performTransaction(transaction).subscribe({
        next: (response) => {
            this.transactionError = "";
            this.transactionSuccess = "Transaction performed successfully";
            setTimeout(() => {
              this.transactionSuccess = '';
              this.router.navigate(['/bank']);
            }, 1500);
        },
        error: (error) => {
          this.transactionSuccess = "";
          this.transactionError = error.error;
          console.log('Error performing transactions', error)
        }
      });
    }
  }
}