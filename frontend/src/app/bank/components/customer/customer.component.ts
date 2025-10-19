import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { FormsModule } from "@angular/forms";
import { BankService } from "../../services/bank.service";
import { Customer } from "../../types/Customer";

@Component({
    selector: 'component-customer',
    templateUrl: './customer.component.html',
    styleUrls: ['./customer.component.scss']
})
export class CustomersComponent implements OnInit {
    isFormSubmitted: boolean | undefined;
    customerSuccess: string = '';
    customerError: string = '';
    customerForm!: FormGroup;
    customers: Customer[] = [];

    constructor(private formBuilder: FormBuilder, private bankService: BankService) {}
    
    ngOnInit(): void {
        this.customerForm = this.formBuilder.group({
            name: ['', [Validators.required]],
            email: ['', [Validators.required, Validators.email]],
            username: ['', [Validators.required, this.noSpecialCharacters]],
            password: ['', [Validators.required, Validators.pattern(/^\w.{8,}$/)]],
            role: ['']
        })
    }

    private noSpecialCharacters(control: any): {[key: string]: boolean} | null {
        if (control.value && /[\W_]/.test(control.value)) {
            return { specialCharacters: true };
          }
          return null;
    }

    onSubmit(): void {
        if (this.customerForm.valid) {
            this.banksService.addCustomer(this.customerForm.value).subscribe({
                next: (data) => {
                  this.customerSuccess = 'Customer created successfully';
                  this.customerError = '';
                  this.customers.push(data)
                  this.isFormSubmitted = true;
                  this.customerForm.reset();
                },
                error: () => {
                    this.customerError = 'Failed to create customer.';
                    this.customerSuccess = '';
                }
            });
        }
        else {
            this.customerError = 'Please fill out all required fields correctly.';
            this.customerSuccess = '';
        }
    }

}
