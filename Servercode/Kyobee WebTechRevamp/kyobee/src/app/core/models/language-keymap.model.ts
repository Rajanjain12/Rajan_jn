export class LanguageKeyMappingDTO {
  langId: number;
  langName: String;
  langIsoCode: string;
  languageMap: Map<String, String>;
  checked: boolean;
  disabled:boolean;
}
