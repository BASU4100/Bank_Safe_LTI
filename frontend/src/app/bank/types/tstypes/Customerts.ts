export class CustomerTS {
    customerId?: string;
    name: string;
    email: string;
    username: string;
    password: string;
    role?: string;

    constructor(name: string, email: string, username: string, password: string, role: string, customerId?: string) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.customerId = customerId;
    }

    displayInfo(): void {
        console.log(`Customer Details:\nCustomer ID : ${this.customerId}\nName : ${this.name}\nEmail : ${this.email}\nUsername : ${this.username}\nPassword : ${this.password}`);
    }
}