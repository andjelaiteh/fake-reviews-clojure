# Fake Reviews Detection – Logistic Regression

## Opis projekta
Ovaj projekat predstavlja implementaciju sistema za analizu i klasifikaciju recenzija sa ciljem detekcije lažnih recenzija. Razvijen je model zasnovan na algoritmu logističke regresije koji vrši klasifikaciju recenzija na fake i real. Sistem obuhvata ceo proces obrade podataka, uključujući učitavanje dataseta, pripremu i analizu podataka, treniranje modela i evaluaciju performansi.

## Korišćene tehnologije i alati
- Clojure
- IntelliJ IDEA
- Leiningen
- Midje

## Dataset
Za potrebe projekta korišćen je **Fake Reviews Dataset** koji je preuzet sa **Kaggle platforme**.
Dataset koji je korišćen u projektu nalazi se u folderu resources:
[Fake Reviews Dataset](resources/fake_reviews_dataset.csv)

## Struktura projekta
Projekat je organizovan kroz više namespace-ova:

- data-source – učitavanje CSV fajla, 
- preprocess – priprema i transformacija podataka za analizu,  
- analytics – funkcije za analizu recenzija,
- logistic-regression – implementacija algoritma logističke regresije  
- core – glavni deo aplikacije koji povezuje sve module i pokreće sistem  

## Dokumentacija
Detaljna dokumentacija projekta nalazi se u sledećem dokumentu:
[Analiza i klasifikacija recenzija – dokumentacija](doc/FAKEREVIEWS-dokumentacija.pdf)

doc/seminarski-fakereviews.pdf
