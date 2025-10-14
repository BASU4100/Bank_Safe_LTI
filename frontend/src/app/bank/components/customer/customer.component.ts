import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { CustomerTS } from "../../types/tstypes/Customerts";
import { FormsModule } from "@angular/forms";

@Component({
    selector: 'component-customer',
    templateUrl: './customer.component.html',
    styleUrls: ['./customer.component.scss']
})
export class CustomersComponent implements OnInit {
    isFormSubmitted: boolean | undefined;
    customerSuccess$: any;
    customerError$: any;
    customerForm!: FormGroup;
    customer: CustomerTS;

    constructor(private formBuilder: FormBuilder) {}
    
    ngOnInit(): void {
        this.customerForm = this.formBuilder.group({
            name: ['', [Validators.required]],
            email: ['', [Validators.required, Validators.email]],
            username: ['', [Validators.required]],
            password: ['', [Validators.required, Validators.pattern(/^\w.{8,}$/)]]
        })
    }

    onSubmit(): void {
        if (this.customerForm.valid) {
            this.customerSuccess$ = 'customer submitted successfully.';
            this.isFormSubmitted = true;
            this.customer = new CustomerTS(this.customerForm.get('name')?.value, this.customerForm.get('email')?.value, this.customerForm.get('username')?.value, this.customerForm.get('password')?.value, '');
        }
        else {
            this.customerError$ = 'All fields are required.'
        }
    }

}
