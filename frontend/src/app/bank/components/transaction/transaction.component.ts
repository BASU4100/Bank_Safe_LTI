import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TransactionTS } from "../../types/tstypes/Transactionts";
import { Transaction } from "../../types/Transaction";
import { FormsModule, ReactiveFormsModule, FormControl } from "@angular/forms";
import { CommonModule } from "@angular/common";

@Component({
    selector: 'app-transaction',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './transaction.component.html',
    styleUrls: ['./transaction.component.scss']
})
export class TransactionComponent implements OnInit{
    transactionForm: FormGroup;
    transaction: TransactionTS;

    constructor(private fb: FormBuilder) {}
    
    ngOnInit(): void {
        this.transactionForm = this.fb.group({
            accountId: [null, Validators.required],
            transactionType: [null, Validators.required],
            amount: [null, [Validators.required, Validators.min(0)]],
            transactionDate: [null, [Validators.required]],
            transactionId: [null],
        });
    }

    onSubmit() {
        if (this.transactionForm.valid) {
            this.transaction = new TransactionTS(this.transactionForm.get('accountId')?.value, this.transactionForm.get('amount')?.value, this.transactionForm.get('transactionDate')?.value, this.transactionForm.get('transactionType')?.value);
        }
    }
}
