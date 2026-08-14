const notificationHTML = `
  <div
    class="form-notification"
    id="formNotification"
    role="alert"
    aria-live="polite"
  >
    <div
      class="notification-icon"
      id="notificationIcon"
    ></div>

    <div class="notification-content">
      <h3 id="notificationTitle"></h3>
      <p id="notificationMessage"></p>
    </div>

    <button
      type="button"
      class="notification-close"
      id="notificationClose"
      aria-label="Cerrar notificación"
    >
      &times;
    </button>
  </div>
`;

let notification;
let notificationIcon;
let notificationTitle;
let notificationMessage;
let notificationClose;

let notificationTimeout;


/**
 * Inicializa el componente de notificaciones.
 */
export function initNotifications() {
  document.body.insertAdjacentHTML(
    "beforeend",
    notificationHTML
  );

  notification = document.querySelector("#formNotification");
  notificationIcon = document.querySelector("#notificationIcon");
  notificationTitle = document.querySelector("#notificationTitle");
  notificationMessage = document.querySelector("#notificationMessage");
  notificationClose = document.querySelector("#notificationClose");

  notificationClose.addEventListener(
    "click",
    hideNotification
  );
}


/**
 * Muestra una notificación.
 *
 * @param {"success"|"error"} type
 * @param {string} title
 * @param {string} message
 */
export function showNotification(type, title, message) {
  if (!notification) {
    console.error(
      "La notificación no ha sido inicializada."
    );

    return;
  }

  clearTimeout(notificationTimeout);

  notification.classList.remove("error");

  if (type === "error") {
    notification.classList.add("error");
    notificationIcon.textContent = "!";
  } else {
    notificationIcon.textContent = "✓";
  }

  notificationTitle.textContent = title;
  notificationMessage.textContent = message;

  notification.classList.add("show");

  notificationTimeout = setTimeout(() => {
    hideNotification();
  }, 5000);
}


/**
 * Oculta la notificación.
 */
export function hideNotification() {
  if (!notification) {
    return;
  }

  notification.classList.remove("show");
}