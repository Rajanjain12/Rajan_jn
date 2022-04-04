/// <reference types="@types/googlemaps" />

import { Component, ViewChild, EventEmitter, Output, OnInit, AfterViewInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
declare let google: any;
// style="padding: 12px 20px; border: 1px solid #ccc; width:200px"
@Component({

    // width:220%;
    selector: 'AutocompleteComponent',
    template: `
      <input 
        type="text"
        [(ngModel)]="autocompleteInput"   
        #addresstext style="
        padding: 6px;
        border-radius: 7px;
        outline: none;
        border: none;" class="demo"
        >
    `,
    styles: ['.demo{width:70%}'],
})
export class AutocompleteComponent implements OnInit, AfterViewInit {
    @Input() adressType: string;
    @Output() setAddress: EventEmitter<any> = new EventEmitter();
    @ViewChild('addresstext',{static: true}) addresstext: any;
    searchControl = new FormControl('');
//google: any;
    autocompleteInput: string;
    queryWait: boolean;

    constructor() {
    }

    ngOnInit() {
    }

    ngAfterViewInit() {
        this.getPlaceAutocomplete();
    }

    private getPlaceAutocomplete() {
        const autocomplete = new google.maps.places.Autocomplete(this.addresstext.nativeElement,
            {
                componentRestrictions: { country: 'IN'},
                types: [this.adressType]  // 'establishment' / 'address' / 'geocode'
            });
            
        google.maps.event.addListener(autocomplete, 'place_changed', () => {
            const place = autocomplete.getPlace();
            this.invokeEvent(place);
        });
       
    }

    invokeEvent(place: Object) {
        this.setAddress.emit(place);
    }
    public displayNull()
    {
       this.addresstext="";
    }
}