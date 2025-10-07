export class TransactionTS {
    transactionId?: number;
    accountId: string;
    amount: number;
    transactionDate: Date;
    transactionType: string;

    constructor(accountId: string, amount: number, transactionDate: Date, transactionId?: number) {
        this.accountId = accountId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.transactionId = transactionId;
    }

    displayInfo(): void {
        console.log(`Transaction Deatils:\nTransaction ID : ${this.transactionId}\nAccount ID : ${this.accountId}\nAmount : ${this.amount.toFixed(2)}\nTransaction Date : ${this.transactionDate}`);
    }
}