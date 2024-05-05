import os

def main():
    base_dir = os.path.dirname(os.path.abspath(__file__))
    build_dir = os.path.join(base_dir, '..', 'react-webapp', 'build')
    css_dir = os.path.join(build_dir, 'static', 'css')
    js_dir = os.path.join(build_dir, 'static', 'js')
    prop_file = os.path.join(base_dir, '..', 'resources', 'maven.properties')

    css_files = [f for f in os.listdir(css_dir) if f.endswith('.css')]
    js_files = [f for f in os.listdir(js_dir) if f.endswith('.js')]
    main_js_files = [f for f in js_files if 'chunk' not in f]  # Exclude chunk files
    chunk_js_files = [f for f in js_files if 'chunk.js' in f]

    with open(prop_file, 'w') as f:
        if css_files:
            f.write('react.css=static/css/{}\n'.format(css_files[0]))
        if main_js_files:
            f.write('react.js=static/js/{}\n'.format(main_js_files[0]))  # Write the main JS file
        if chunk_js_files:
            f.write('react.chunk.js=static/js/{}\n'.format(chunk_js_files[0]))

    print("CSS files found:", css_files)
    print("Main JS files found:", main_js_files)
    print("Chunk JS files found:", chunk_js_files)

if __name__ == "__main__":
    main()
