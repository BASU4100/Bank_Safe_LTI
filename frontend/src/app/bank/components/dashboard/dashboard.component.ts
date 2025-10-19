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
    customers: Customer[];
    accounts: Account[];
    transactions: Transaction[];

    role: string | null;
    userId: number;

    constructor(private bankService: BankService, private router: Router) { }

    ngOnInit(): void {
        this.role = localStorage.getItem("role");
        this.userId = Number(localStorage.getItem("user_id"));
        if (this.role === 'ADMIN') {
            console.log('loadAdminData');
            this.loadAdminData();
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
}