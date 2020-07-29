export class CountryDTO {
  countryName: string;
  isocode: string;

  CountryDTO() {
    this.countryName = 'United States';
    this.isocode = 'us';
  }
}
