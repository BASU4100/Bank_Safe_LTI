import { Account } from './Account';

export class Transaction {
    amount: number;
    transactionDate?: Date;
    transactionType: string;
    accounts: Account;
    constructor(data: any)
    {
        this.amount=data.amount;
        this.transactionDate=new Date();
        this.transactionType=data.transactionType;
        this.accounts=data.accounts;
    }
}