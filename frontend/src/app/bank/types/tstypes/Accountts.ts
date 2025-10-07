export class AccountTS {
    accountId?: string;
    customerId: string;
    balance: number;

    constructor(customerId: string, balance: number, accountId?: string) {
        this.customerId = customerId;
        this.balance = balance;
        this.accountId = accountId;
    }

    displayInfo(): void{
        console.log(`Account Details:\nAccount ID : ${this.accountId}\nCustomer ID : ${this.customerId}\nBalance : ${this.balance.toFixed(2)}`);
    }
}