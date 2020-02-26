import {
  ElementRef,
  HostListener,
  Directive,
  Input,
  NgZone,
  OnDestroy,
  OnChanges,
  AfterContentChecked
} from '@angular/core';

const MAX_LOOKUP_RETRIES = 3;

@Directive({
  selector: '[appAutosize]'
})
export class AutosizeDirective implements OnDestroy, OnChanges, AfterContentChecked {
  @Input()
  set minRows(value) {
    this.privateMinRows = value;
    if (this.textAreaEl) {
      this.textAreaEl.rows = value;
    }
  }
  private privateMinRows: number;

  @Input() maxRows: number;
  @Input() onlyGrow = false;
  @Input() useImportant = false;

  private retries = 0;
  private textAreaEl: any;

  private privateOldContent: string;
  private privateOldWidth: number;

  private privateWindowResizeHandler;
  private privateDestroyed = false;

  @HostListener('input', ['$event.target'])
  onInput(textArea: HTMLTextAreaElement): void {
    this.adjust();
  }

  constructor(public element: ElementRef, private privateZone: NgZone) {
    if (this.element.nativeElement.tagName !== 'TEXTAREA') {
      this._findNestedTextArea();
    } else {
      this.textAreaEl = this.element.nativeElement;
      this.textAreaEl.style.overflow = 'hidden';
      this._onTextAreaFound();
    }
  }

  ngOnDestroy() {
    this.privateDestroyed = true;
    if (this.privateWindowResizeHandler) {
      window.removeEventListener('resize', this.privateWindowResizeHandler, false);
    }
  }

  ngAfterContentChecked() {
    this.adjust();
  }

  ngOnChanges(changes) {
    this.adjust(true);
  }

  _findNestedTextArea() {
    this.textAreaEl = this.element.nativeElement.querySelector('TEXTAREA');

    if (!this.textAreaEl && this.element.nativeElement.shadowRoot) {
      this.textAreaEl = this.element.nativeElement.shadowRoot.querySelector('TEXTAREA');
    }

    if (!this.textAreaEl) {
      if (this.retries >= MAX_LOOKUP_RETRIES) {
        console.warn('ngx-autosize: textarea not found');
      } else {
        this.retries++;
        setTimeout(() => {
          this._findNestedTextArea();
        }, 100);
      }
      return;
    }

    this.textAreaEl.style.overflow = 'hidden';
    this._onTextAreaFound();
  }

  _onTextAreaFound() {
    this._addWindowResizeHandler();
    setTimeout(() => {
      this.adjust();
    });
  }

  _addWindowResizeHandler() {
    this.privateWindowResizeHandler = Debounce(() => {
      this.privateZone.run(() => {
        this.adjust();
      });
    }, 200);

    this.privateZone.runOutsideAngular(() => {
      window.addEventListener('resize', this.privateWindowResizeHandler, false);
    });
  }

  adjust(inputsChanged = false): void {
    if (!this.privateDestroyed && this.textAreaEl) {
      const currentText = this.textAreaEl.value;

      if (
        inputsChanged === false &&
        currentText === this.privateOldContent &&
        this.textAreaEl.offsetWidth === this.privateOldWidth
      ) {
        return;
      }

      this.privateOldContent = currentText;
      this.privateOldWidth = this.textAreaEl.offsetWidth;

      const clone = this.textAreaEl.cloneNode(true);
      const parent = this.textAreaEl.parentNode;
      clone.style.width = this.textAreaEl.offsetWidth + 'px';
      clone.style.visibility = 'hidden';
      clone.style.position = 'absolute';
      clone.textContent = currentText;

      parent.appendChild(clone);

      clone.style.overflow = 'auto';
      clone.style.height = 'auto';

      let height = clone.scrollHeight;

      // add into height top and bottom borders' width
      const computedStyle = window.getComputedStyle(clone, null);
      // tslint:disable-next-line: radix
      height += parseInt(computedStyle.getPropertyValue('border-top-width'));
      // tslint:disable-next-line: radix
      height += parseInt(computedStyle.getPropertyValue('border-bottom-width'));

      const oldHeight = this.textAreaEl.offsetHeight;
      const willGrow = height > oldHeight;

      if (this.onlyGrow === false || willGrow) {
        const lineHeight = this._getLineHeight();
        const rowsCount = height / lineHeight;

        if (this.privateMinRows && this.privateMinRows >= rowsCount) {
          height = this.privateMinRows * lineHeight;
        } else if (this.maxRows && this.maxRows <= rowsCount) {
          // never shrink the textarea if onlyGrow is true
          const maxHeight = this.maxRows * lineHeight;
          height = this.onlyGrow ? Math.max(maxHeight, oldHeight) : maxHeight;
          this.textAreaEl.style.overflow = 'auto';
        } else {
          this.textAreaEl.style.overflow = 'hidden';
        }

        let heightStyle = height + 'px';
        heightStyle += this.useImportant ? ' !important' : '';

        this.textAreaEl.style.height = heightStyle;
      }

      parent.removeChild(clone);
    }
  }

  private _getLineHeight() {
    let lineHeight = parseInt(this.textAreaEl.style.lineHeight, 10);
    if (isNaN(lineHeight) && window.getComputedStyle) {
      const styles = window.getComputedStyle(this.textAreaEl);
      lineHeight = parseInt(styles.lineHeight, 10);
    }

    if (isNaN(lineHeight)) {
      const fontSize = window.getComputedStyle(this.textAreaEl, null).getPropertyValue('font-size');
      lineHeight = Math.floor(parseInt(fontSize.replace('px', ''), 10) * 1.5);
    }

    return lineHeight;
  }
}

function Debounce(func, wait, immediate = false) {
  let timeout;
  return function() {
    const context = this;
    const args = arguments;
    const later = () => {
      timeout = null;
      if (!immediate) {
        func.apply(context, args);
      }
    };
    const callNow = immediate && !timeout;
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
    if (callNow) {
      func.apply(context, args);
    }
  };
}
