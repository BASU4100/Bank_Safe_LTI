export class Customer {
    customerId?:string;
    name: string;
    email:string;
    password: string;
    username: string;
    role?: string;
    constructor(data: any)
    {
        this.customerId=data.customerId;
        this.name=data.name;
        this.email=data.email;
        this.password=data.password;
        this.username=data.password;
        this.role=data.role;
    }
    
    displayInfo() : void {
        console.log(this.name);
    }
}