import { Component, OnInit } from '@angular/core';
import { BankService } from '../../services/bank.service';
import { Customer } from '../../types/Customer';
import { Account } from '../../types/Account';
import { Transaction } from '../../types/Transaction';
import { Router } from '@angular/router';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
    customers: Customer[] = [];
    accounts: Account[] = [];
    transactions: Transaction[] = [];
    loggedInCustomer: Customer;

    role: string;
    userId: string;

    constructor(private bankService: BankService, private router: Router) { }

    ngOnInit(): void {
        this.role = localStorage.getItem("role") as string;
        this.userId = localStorage.getItem("user_id") as string;
        if (this.role === 'ADMIN') {
            console.log('loadAdminData');
            this.loadAdminData();
        }
        else {
            console.log('loadUserData');
            this.loadUserData();
        }
    }

    loadAdminData(): void {
        this.bankService.getAllCustomers().subscribe({
            next: (response) => {
                this.customers = response;
            },
            error: (error) => console.log('Error loading customers', error)
        });

        this.bankService.getAllAccounts().subscribe({
            next: (response) => {
                this.accounts = response;
            },
            error: (error) => console.log('Error loading accounts', error)
        });

        this.bankService.getAllTranactions().subscribe({
            next: (response) => {
                this.transactions = response;
            },
            error: (error) => console.log('Error loading transactions', error)
        });
    }

    loadUserData(): void {
        this.bankService.getCustomerById(Number(this.userId)).subscribe({
            next: (response) => {
                this.loggedInCustomer = response;
            },
            error: (error) => console.log('Error loading logged in customer details', error)
        });
        this.bankService.getAccountsByUser(this.userId).subscribe({
            next: (response) => {
                this.accounts = response;
            },
            error: (error) => console.log('Error loading account for user', error)
        })

        this.bankService.getAllTransactionsByCustomerId(this.userId).subscribe({
            next: (response) => {
                this.transactions = response;
            },
            error: (error) => console.log('Error loading transactions by user', error)
        });
    }

    onDeleteUser(customerId: string): void {
        if(confirm("Are you sure you want to delete the User!!!")) {
            this.bankService.deleteCustomer(+customerId).subscribe({
                next: () => alert("Customer Delete Successfully!"),
                error: () => alert("Error Occured while deleting.")
            });
            this.ngOnInit();
        }
    }

    onDeleteAccount(accountId: number): void {
        if(confirm("Are you sure you want to delete the Account!!!")) {
            this.bankService.deleteAccount(accountId).subscribe({
                next: () => alert("Account Delete Successfully!"),
                error: () => alert("Error Occured while deleting.")
            });
            this.ngOnInit();
        }
    }
}