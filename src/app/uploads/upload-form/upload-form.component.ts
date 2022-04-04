import { Component, OnInit } from '@angular/core';

import { UploadService } from '../shared/upload.service';
import { Upload } from '../shared/upload';
import * as _ from "lodash";

@Component({
  selector: 'app-upload-form',
  templateUrl: './upload-form.component.html',
  styleUrls: ['./upload-form.component.css']
})
export class UploadFormComponent implements OnInit {

  selectedFiles: FileList;
  currentUpload: Upload;

  constructor(private upSvc: UploadService) { }


  uploadSingle() {
    let file = this.selectedFiles.item(0)
    this.currentUpload = new Upload(file);
    this.upSvc.pushUpload(this.currentUpload)
  }

  uploadMulti() {
    let files = this.selectedFiles
    let filesIndex = _.range(files.length)
    _.each(filesIndex, (idx) => {
      this.currentUpload = new Upload(files[idx]);
      this.upSvc.pushUpload(this.currentUpload)}
    )
  }

  ngOnInit() {
  }

}
