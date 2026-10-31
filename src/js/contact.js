import { initNotifications, showNotification } from "../utils/notificaciones.js";

initNotifications();

emailjs.init({
  publicKey: "1yAXMYL8iGTD1QQ42",
  // Bloquea navegadores automatizados (bots)
  blockHeadless: true,
  // Máximo un envío cada 30 segundos desde este navegador
  limitRate: {
    id: "contact-form",
    throttle: 30000,
  },
  // init() borra el almacenamiento por defecto; sin esto limitRate no funciona
  storageProvider: {
    get: (key) => Promise.resolve(localStorage.getItem(key)),
    set: (key, value) => Promise.resolve(localStorage.setItem(key, value)),
    remove: (key) => Promise.resolve(localStorage.removeItem(key)),
  },
});

const contactForm = document.querySelector(".contact-form");
const submitButton = contactForm.querySelector('button[type="submit"]');
const submitButtonText = submitButton.textContent;

contactForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  if (submitButton.disabled || !validateForm()) {
    return;
  }

  setSending(true);

  try {
    await emailjs.sendForm(
      "service_z34lvvl",
      "template_2lbrv1c",
      contactForm,
    );

    showNotification(
      'success',
      '¡Mensaje enviado!',
      'Gracias por contactarme. Me pondré en contacto contigo pronto.'
    );

    contactForm.reset();
  } catch (error) {
    console.error("Error al enviar el mensaje:", error);

    if (error?.status === 429) {
      showNotification(
        'error',
        'Espera un momento',
        'Ya enviaste un mensaje hace poco. Inténtalo de nuevo en unos segundos.'
      );

      return;
    }

    showNotification(
      'error',
      'No se pudo enviar',
      'Ocurrió un problema. Inténtalo nuevamente.'
    );
  } finally {
    setSending(false);
  }
});

function setSending(isSending) {
  submitButton.disabled = isSending;
  submitButton.textContent = isSending ? "Enviando…" : submitButtonText;
  contactForm.setAttribute("aria-busy", String(isSending));
}

function validateName(name) {
  return name.trim().length >= 3;
}

function validatePhone(phone) {
  // Acepta espacios, guiones, paréntesis y un + inicial: +57 312 273 3087
  const cleanPhone = phone.replace(/[\s\-()]/g, "");
  const phoneRegex = /^\+?[0-9]{7,15}$/;

  return phoneRegex.test(cleanPhone);
}

function validateEmail(email) {
    const emailRegex =
        /^[a-zA-Z0-9._%+-]+@[a-zA-Z][a-zA-Z0-9-]*(\.[a-zA-Z0-9-]+)*\.[a-zA-Z]{2,}$/;

    return emailRegex.test(email.trim());
}

function validateService(service) {
  return service !== "";
}

function validateMessage(message) {
  return message.trim().length >= 10;
}

function validateForm() {
  const name = document.querySelector("#name").value;
  const phone = document.querySelector("#phone").value;
  const email = document.querySelector("#email").value;
  const service = document.querySelector("#service").value;
  const message = document.querySelector("#mensaje").value;

  if (!validateName(name)) {
    showNotification(
      "error",
      "Nombre inválido",
      "Ingresa un nombre válido."
    );

    return false;
  }

  if (!validatePhone(phone)) {
    showNotification(
      "error",
      "Celular inválido",
      "Ingresa un número válido, por ejemplo +57 312 273 3087."
    );

    return false;
  }

  if (!validateEmail(email)) {
    showNotification(
      "error",
      "Email inválido",
      "Ingresa un correo electrónico válido."
    );

    return false;
  }

  if (!validateService(service)) {
    showNotification(
      "error",
      "Selecciona un servicio",
      "Selecciona el servicio que necesitas."
    );

    return false;
  }

  if (!validateMessage(message)) {
    showNotification(
      "error",
      "Mensaje demasiado corto",
      "Escribe un mensaje de al menos 10 caracteres."
    );

    return false;
  }

  return true;
}