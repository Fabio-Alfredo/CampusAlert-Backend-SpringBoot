INSERT INTO public.roles(id, name) VALUES('ADMIN', 'administrador')
    ON CONFLICT (id) DO UPDATE SET name = excluded.name;

INSERT INTO public.roles(id, name) VALUES('USER', 'usuario')
    ON CONFLICT (id) DO UPDATE SET name = excluded.name;